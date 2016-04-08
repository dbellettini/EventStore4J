Apt::Ppa <| |> -> Package <| |>
Apt::Key<| |> -> Package<| |>
Class['apt::update'] -> Package<| |>

class { 'java': } ->
package {['ant', 'maven']:
  ensure => installed
}

class { 'l10n': }

$toolpkg = [
  'dos2unix',
  'git',
  'htop',
  'screen',
  'tig',
  'tmux',
  'unzip',
]

package { $toolpkg:
  ensure => present
}

class { 'postgresql::globals':
  manage_package_repo => true,
  version             => '9.5'
}->
class { 'postgresql::server': }

postgresql::server::db { 'eventstore4j':
  user     => 'eventstore4j',
  password => postgresql_password('eventstore4j', 'eventstore4j'),
}
